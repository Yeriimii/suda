import { Client } from '@stomp/stompjs';

const connect = (url, onMessageReceived) => {
  let subscribeId;
  let chatRoomId = "1";
  const client = new Client({
    brokerURL: url,
    onConnect: () => {
      console.log('Connected to STOMP server');
      // TODO: subscribe destination 은 채팅방의 아이디로 하면 1:1 채팅이 된다. 현재는 브로드캐스팅이다.
      // TODO: 또는 spring controller 에서 @SendTo or @SendToUser 로 한다.
      const subscription = client.subscribe(`/exchange/chat.exchange/room.${chatRoomId}`,
          message => { onMessageReceived(JSON.parse(message.body)); },
        { 'auto-delete': false, 'durable': true, 'exclusive': false });

      subscribeId = subscription.id;
    },
    onStompError: (error) => {
      console.error('STOMP error', error);
    }
  });

  client.activate();

  const sendMessage = (destination, body) => {
    client.publish({
      destination,
      headers: {
        'Authorization' : localStorage.getItem('refresh'),
        'content-type': 'application/json'
      },
      body: JSON.stringify(body)
    });
  };

  const disconnect = () => {
    client.unsubscribe(`${subscribeId}`);
    client.deactivate();
  };

  const isConnected = () => {
    return client.connected;
  };

  return {
    sendMessage,
    disconnect,
    isConnected,
  };
};

export { connect };
