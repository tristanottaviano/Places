
//Close the modal window
function cancelCommentModal() {
    
    $(".modal").hide();        

} 

//Close the modal window
function cancelAddCommentModal() {
    
    $(".modal").hide();

    //Clear the textBoxes
    $("#addCommentModalTitle").val('');
    $("#addCommentModalComment").val('');

} 

//Add a Friend
function addComment(){
    
    //Get the values inputed by the user
    var title = $("#addCommentModalTitle").val();
    var text = $("#addCommentModalComment").val();
    var note = $("input[name='rating']:checked").val();    
    
    if (title == ""){
        
        alert("You must add a valid Title!");
        
    }
    
    else if(title.length > 25){
        
        alert("Your comment's Title is too long! (25 characters max.)");
        
    }
    
    else if(text.length > 200){
        
        alert("Your comment is too long! (200 character max.)");
        
    }
    
    else{
        
        //Clear the textBoxes
        $("#addCommentModalTitle").val('');
        $("#addCommentModalComment").val('');
        
        //Define and intialize objectId
        var objectId;
        var objectType;
        if (currentPin===null){
    
            objectId = currentMap.id;
            objectType = "maps";
    
        }
        else {
    
            objectId = currentPin.id;
            objectType = "pins";
    
        }

        //Creating a new object to send
        var myComment = {
            
            id: 0,
            user: currentUser.id,
            title: title,
            text: text,
            note: note,
            objectId: objectId
            
        };
        

        //Send the pin WS
        postServerData("ws/services/"+objectType+"/comment", JSON.stringify(myComment), function(comment){
        
            $("#addCommentModal").hide();

        });
        
    }

}



//List all the maps of the current user
function listComment(){
    
    //Define and intialize objectId
    var objectId;
    var objectType;
    if (currentPin===null){

        objectId = currentMap.id;
        objectType = "maps";

    }
    else {

        objectId = currentPin.id;
        objectType = "pins";

    }

    //Get the comments from the DB
    getServerData("ws/services/"+objectType+"/getComment/"+objectId, function(listArray){

        //Empty the list
        $("#commentModalCommentList").empty();

        var commentArray = JSON.parse(JSON.stringify(listArray));
        
        $.each(commentArray ,function(i,v) {
            getServerData("ws/services/users/search/"+v.user,function(author){
                $("#commentModalCommentList").append('<li class="listItem"> <a>' + author.username + " - " +v.title + ' - ' + v.note + '.0 </a> <div class="listItemDescription">' + v.text + '</div> </li>') ;
            });
        });    
    
    });

    //Show the modal
    $("#commentModal").show();


}
//jQuery (runs when page is loaded)
$(document).ready(function($) {
 
    //myFriends list
    $("#commentButton").click(listMyFriends);
    $("#commentModalCancelButton").click(cancelFriendsModal);
    $("#closeCommentModal").click(cancelFriendsModal);


    //Create user
    $("#commentModalAddCommentButton").click(function(){
        
        cancelCommentModal();
        $("#addCommentModalTitle").val('');
        $("#addCommentModalText").val('');
        $("#addCommentModal").show();

    });
    $("#addCommentModalCancelButton").click(cancelAddCommentModal);
    $("#closeAddCommentModal").click(cancelAddCommentModal);
    $("#addCommentModalAddCommentButton").click(addComment);
 
});