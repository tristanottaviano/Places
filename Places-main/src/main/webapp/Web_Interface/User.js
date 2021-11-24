function createUser(){

    //Get the values inputed by the user
    var username = $("#createUserModalUsername").val();
    var bio = $("#createUserModalBio").val();
    var password = $("#createUserModalPassword").val();


    if (username == ""){

        alert("You must add a valid Username!");

    }

    else if(username.length > 25){

        alert("Your Username is too long! (25 characters max.)");

    }
    
    else if(bio.length > 200){

        alert("Your Bio is too long! (200 character max.)");

    }

    else if(password.length == 0){

        alert("You must enter a password!");

    }

    else{
        
        //Hide the modal window
        $("#createUserModal").hide();

        //Clear the textBoxes
        $("#createUserModalUsername").val('');
        $("#createUserModalBio").val('');
        $("#createUserModalPassword").val('');
        
        //Creating a new object to send
        var myUser = {

            id: 0,
            username: username,
            password: password,
            bio: bio,
            contacts: null,
            myMaps: null,
            myComments: null,
            myRequests: null
            
        };
        
        //Send the pin WS
        postServerData("ws/services/users/create", JSON.stringify(myUser), function(user){

            if (user != null){

                //If the user is not connected yet, he gets logged in the new account
                if (currentUser === null){

                    currentUser = user;
                    refreshCurrentUserPanel(currentUser);
                    showConnectedButtons();
                
                }

            }
            
            else {
                
                alert("This user already exists");

            }

        });
        
    }

}


//LoggingIn
function login(){

    //Get the values inputed by the user
    var username = $("#loginModalUsername").val();
    var password = $("#loginModalPassword").val();


    if (username == ""){

        alert("You must add a valid Username!");

    }

    else if(password.length == 0){

        alert("You must enter a password!");

    }

    else{
        
        //Hide the modal window
        $("#loginModal").hide();

        //Clear the textBoxes
        $("#loginModalUsername").val('');
        $("#loginModalPassword").val('');
        
        //Creating a new object to send
        var myLogin = {

            username: username,
            password: password
            
        };
        
        //Send the pin WS
        postServerData("ws/services/users/connect", JSON.stringify(myLogin), function(user){

            if (user != null) { 

                currentUser = user;
                refreshCurrentUserPanel(currentUser);
                showConnectedButtons();

                currentMap = null;
                refreshCurrentPinPanel(currentMap);
                refreshMarkers(currentMap);

            }

            else {

                alert("Wrong Username or Password");

            }

        });
        
    }

}

//Logging out the currentUser
function logout(){

    currentUser = null;
    currentMap = null;
    refreshCurrentUserPanel(null);
    refreshCurrentPinPanel(null);
    refreshMarkers(null);
    hideConnectedButtons();

}

//Close the modal window
function cancelCreateUserModal() {
    
    $(".modal").hide();

    //Clear the textBoxes
    $("#createUserModalUsername").val('');
    $("#createUserModalBio").val('');
    $("#createUserModalPassword").val('');

} 


//Close the modal window
function cancelLoginModal() {
    
    $(".modal").hide();

    //Clear the text
    $("#loginModalUsername").val('');
    $("#loginModalPassword").val('');
        
} 

//jQuery (runs when page is loaded)
$(document).ready(function($) {

    //Create user
    $("#loginModalcreateUserButton").click(function(){
        
        cancelLoginModal();
        $("#createUserModal").show();

    });
    $("#createUserModalCancelButton").click(cancelCreateUserModal);
    $("#closeCreateUserModal").click(cancelCreateUserModal);
    $("#createUserModalCreateUserButton").click(createUser);

    //Login
    $("#loginButton").click(function(){
        
        $("#loginModal").show();

    });
    $("#loginModalCancelButton").click(cancelLoginModal);
    $("#closeLoginModal").click(cancelLoginModal);
    $("#loginModalLoginButton").click(login);

    $("#logoutButton").click(logout);

});