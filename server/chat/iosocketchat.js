const socketio = require('socket.io')

var currentSockets = {}

function IOSocketServer(server){
    var io = socketio.listen(server)

    io.on('connection',function(socket) {    

        var userId='';
    
        socket.on('start', function(data) {
            const messageData = JSON.parse(data);
            userId = messageData.senderId;
    
            //socket.join(`${userId}`);
            currentSockets[userId]=socket.id;
    
            //check and load message on db, send to client
        })
    
        socket.on('sendMessage',function(data) {
            //Get input sender, receipient, content
            const messageData = JSON.parse(data);
            const senderId = messageData.senderId;
            const recipientId = messageData.recipientId;
            const messageContent = messageData.messageContent;
            
            //Create output message
            const chatData = {
                senderId : senderId,
                recipientId : recipientId,
                messageContent : messageContent
            }
    
            //Forward to receipient
            console.log(chatData);
            //socket.to(`${recipientId}`).emit('receiveMessage',JSON.stringify(chatData))
    
            //if user online
            if(currentSockets[recipientId]==undefined){
    
            }
            else{
                io.to(currentSockets[recipientId]).emit('receiveMessage',JSON.stringify(chatData));
            }
            
            //if user offline, save to db
    
        })
    
        socket.on('disconnect', function () {
            currentSockets[userId]=undefined;
            console.log(`${userId} disconnected`);
        });
    });
}


module.exports = {
    IOSocketServer: IOSocketServer,
    currentSockets: currentSockets
};