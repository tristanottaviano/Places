//Close the modal window
function cancelFriendsModal() {
    
    $(".modal").hide();        

} 

//Close the modal window
function cancelAddFriendsModal() {
    
    $(".modal").hide();

    //Clear the textBoxes
    $("#addFriendsModalUsername").val('');

} 

//Add a Friend
function sendRequest(){

    //Get the values inputed by the user
    var username = $("#addFriendsModalUsername").val();
    
    //Clear the textBoxes
    $("#addFriendsModalUsername").val('');
    

    if (username == ""){
        
        alert("You must add a valid Username!");
        
    }
    
    else if (username === currentUser.username){

        alert("You can't follow yourself");

    }
    
    
    else{
        
        //Creating a new object to send
        var myRequest = {
            
            senderId: currentUser.id,
            friendUsername: username,
            
        };
        
        //Send the pin WS
        postServerData("ws/services/users/request", JSON.stringify(myRequest), function(friend){
            
            if (friend === null) {
                
                alert("This user does not exist");
                
            }
            
            else if(friend.username === "-1"){
                
                alert("You already sent friend request to " + username);
                
            } 
            
            else if(friend.username === "-2"){
                
                alert("You already follow " + username);
                
            } 
            
            else {

                //Hide the modal window
                $("#addFriendsModal").hide();
                
                alert("Your friend request has been sent to " + friend.username);
                
            }

        });
        
    }

}



//List all the friends of the current user
function listMyFriends(){

    //If the user is not logged in
    if (currentUser === null){

        alert("You must be logged in to see your Friends list!");

    }

   else{
       
        //Get the friends from the DB
        getServerData("ws/services/friends/user/"+currentUser.id, function(listArray){

            //Empty the list
            $("#myFriendsModalFriendsList").empty();

            var myFriendsArray = JSON.parse(JSON.stringify(listArray));
            
            $.each(myFriendsArray ,function(i,v) {
                $("#myFriendsModalFriendsList").append('<li class="listItem" onclick="listUserMaps('+v.id+');"> <a>' + v.username + '</a> <div class="listItemDescription">' + v.bio + '</div> </li>') ;
            });    
        
        });

        //Show the modal
        $("#myFriendsModal").show();
    
    }
    
}


//List all the friend Request
function listFriendRequest(){

    //If the user is not logged in
    if (currentUser === null){

        alert("You must be logged in to see your Friends requests!");

    }

    else{
    
        //Empty the list
        $("#friendRequestModalFriendRequestList").empty();
        
        $.each(currentUser.myRequests ,function(i,v) {
           
            getServerData("ws/services/users/search/"+v, function(sender){

                $("#friendRequestModalFriendRequestList").append('<li class="listItem" onclick="openComfirmModal('+sender.id+');"> <a>' + sender.username + '</a> <div class="listItemDescription">' + sender.bio + '</div> </li>') ;

            });

        });    
    

        //Show the modal
        $("#friendRequestModal").show();
    
    }
    
}

//Open the modal to confirm the friend request
function openComfirmModal(senderId){

    getServerData("ws/services/users/search/"+senderId, function(sender){
        
        $("#friendRequestModal").hide();
        
        $("#confirmFriendModalTitle").text("Confirm " + sender.username + "'s request");
        $("#confirmFriendModalContentBody").text(sender.username + " would like to follow you!");

        //Unbind the buttons
        $("#confirmFriendModalAcceptButton").unbind();
        $("#confirmFriendModalDenyButton").unbind();

        //Bind the buttons to the correct user
        $("#confirmFriendModalAcceptButton").click(function () {acceptFriend(sender);});
        $("#confirmFriendModalDenyButton").click(function () {denyFriend(sender);});
    
        $("#confirmFriendModal").show();
    
    });    

}

function acceptFriend(sender){

    //Add friend
     var myFriend = {
            
        friendId: currentUser.id,
        senderUsername: sender.username,
        
    };
    
    //Send the pin WS
    postServerData("ws/services/friends/add", JSON.stringify(myFriend), function(){
            
        //Hide the modal window
        $("#confirmFriendModal").hide();        
    
        //Refresh the current user
        refreshCurrentUserRequestList();
    

    });

}

function denyFriend(sender){

    //Add friend
    var myFriend = {
            
        friendId: currentUser.id,
        senderUsername: sender.username,
        
    };
    
    //Send the pin WS
    postServerData("ws/services/friends/deny", JSON.stringify(myFriend), function(){
            
        //Hide the modal window
        $("#confirmFriendModal").hide();
    
        //Refreshing the curretnUser
        refreshCurrentUserRequestList();
        
    });

}

//Refresh the current user
function refreshCurrentUserRequestList(){  
    
    getServerData("ws/services/users/search/"+currentUser.id, function(user){
        
        currentUser = user;
        
        $.each(currentUser.myRequests ,function(i,v) {
            
            console.log("Requests: " + v);
            
        });    
        
        
            //Open up the request List
            listFriendRequest();
    });    

}

//jQuery (runs when page is loaded)
$(document).ready(function($) {
 
    //myFriends list
    $("#myFriendsButton").click(listMyFriends);
    $("#myFriendsModalCancelButton").click(cancelFriendsModal);
    $("#closeMyFriendsModal").click(cancelFriendsModal);


    //Create user
    $("#myFriendsModalAddFriendsButton").click(function(){
        
        cancelFriendsModal();
        $("#addFriendsModalUsername").val('');
        $("#addFriendsModal").show();

    });
    $("#addFriendsModalCancelButton").click(cancelAddFriendsModal);
    $("#closeAddFriendsModal").click(cancelAddFriendsModal);
    $("#addFriendsModalAddFriendsButton").click(sendRequest);
    
    //friendrequest list
    $("#friendRequestButton").click(function(){
        
        listFriendRequest();

    });

    $("#friendRequestModalCancelButton").click(cancelFriendsModal);
    $("#closeFriendrequestModal").click(cancelFriendsModal);

    //Confirm friend
    $("#closeConfirmFriendModal").click(cancelFriendsModal);

});