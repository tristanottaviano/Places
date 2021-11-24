
//Create a new map
function createMap(){

    //Get the values inputed by the user
    var title = $("#createMapsModalTitle").val();
    var description = $("#createMapsModalDescription").val();
    var tags = $("#createMapsModalTags").val();
    var status;
    if ($("#createMapsModalPublicSlider").is(":checked")) status=1;
    else status=0

    if (title == ""){

        alert("You must add a valid Title!");

    }

    else if(title.length > 25){

        alert("Your Title is too long! (25 characters max.)");

    }
    
    else if(description.length > 200){

        alert("Your Map's Description is too long! (200 character max.)");

    }

    else{
        
        //Hide the modal window
        $("#createMapsModal").hide();

        //Clear the textBoxes
        $("#createMapsModalTitle").val('');
        $("#createMapsModalDescription").val('');
        $("#createMapsModalTags").val('');
        
        //Creating a new object to send
        var myMap = {

            id: 0,
            status: status,
            title: title,
            description: description,
            tags: tags,
            pins: null,
            comments: null,
            user: currentUser.id
            
        };
        
        //Send the pin WS
        postServerData("ws/services/maps/create", JSON.stringify(myMap), function(map){

            if (map === null){

                alert("There has been a problem!");
                
            }
            
            else {

                openMap(map);

            }

        });
        
    }

}

//Edit a map
function editMap(){

    //Get the values inputed by the user
    var title = $("#editMapsModalTitle").val();
    var description = $("#editMapsModalDescription").val();
    var tags = $("#editMapsModalTags").val();
    var status;
    if ($("#editMapsModalPublicSlider").is(":checked")) status=1;
    else status=0;

    if (title == ""){

        alert("You must add a valid Title!");

    }

    else if(title.length > 25){

        alert("Your Title is too long! (25 characters max.)");

    }
    
    else if(description.length > 200){

        alert("Your Map's Description is too long! (200 character max.)");

    }

    else{
        
        //Hide the modal window
        $("#editMapsModal").hide();

        //Clear the textBoxes
        $("#editMapsModalTitle").val('');
        $("#editMapsModalDescription").val('');
        $("#editMapsModalTags").val('');
        
        //Creating a new object to send
        var myMap = {

            id: currentMap.id,
            status: status,
            title: title,
            description: description,
            tags: tags,
            pins: currentMap.pins,
            comments: currentMap.comments,
            user: currentUser.id
            
        };
        
        //Send the pin WS
        postServerData("ws/services/maps/edit", JSON.stringify(myMap),function(map) {
            
            currentMap = map;
            refreshCurrentPinPanel(map);
    
        });
    
    }

}

//Delete map
function deleteMap(){
    
    getServerData("ws/services/maps/delete/"+currentMap.id, function(){

        alert("The map " + currentMap.title + " has been deleted");
        currentMap = null;
        refreshCurrentPinPanel(null);
        refreshMarkers(null);

    });

}

//Close the modal window
function cancelMapsModal() {
    
    $(".modal").hide();        

} 

//Close the modal window
function cancelCreateMapsModal() {
    
    $(".modal").hide();

    //Clear the textBoxes
    $("#createMapsModalTitle").val('');
    $("#createMapsModalDescription").val('');
    $("#createMapsModalTags").val('');

} 


//Select a map (when clicking on th button, from the list)
function selectMap(id) {

    //Hide the modal
    $("#myMapsModal").hide();

    getServerData("ws/services/maps/search/"+id, openMap);

}

//Open the new map
function openMap(map){

    //Displaying map info
    currentMap = map;
    $("#findWayButton").hide();
    $("#photoButton").hide();
    $("#directionTypeCont").hide();
    refreshCurrentPinPanel(map);
    refreshMarkers(map);

}

//Refrsh all the markeer on the map
function refreshMarkers(map){

    //Clearing all the markers
    for (let i = 0; i < displayMarkers.length; i++) {
            
        displayMarkers[i].setMap(null);

    }

    //Emptying the list of markers
    while(displayMarkers.length > 0) {
        displayMarkers.pop();
    }

    if (map!= null){

        //Set marker icon
        var icon = {
            url: "./Resource/Marker.png",
            scaledSize: new google.maps.Size(35, 35)
        };
    
        //Adding all the pins of the new map
        for (pinId of map.pins){
        
            getServerData("ws/services/pins/search/"+pinId, function(pin){
    
                //Place the marker
                var marker = new google.maps.Marker({
                    id: pin.id,
                    position: {lat: pin.latitude, lng: pin.longitude}, 
                    map: displayMap,
                    icon: icon
                });
    
                //Add properties to the pin
                marker["title"] = pin.title;
                marker["description"] = pin.description; 
                marker["tags"] = pin.tags;
                marker["id"] = pin.id;
    
                //Adding a Listener
                marker.addListener('click', function (e){ selectPin(marker);
                });
    
                //Adding the markers to tthe list
                displayMarkers.push(marker);
    
            });
    
        }
        
    }

}

//List all the public maps
function listPublicMaps(){
    
    //Get the maps from the DB
    getServerData("ws/services/maps/publicMaps", function(listArray){

        //Empty the list
        $("#publicMapsModalMapsList").empty();

        var myMapsArray = JSON.parse(JSON.stringify(listArray));
        
        $.each(myMapsArray ,function(i,v) {
            $("#publicMapsModalMapsList").append('<li class="listItem" onclick="selectMap('+v.id+');"> <a >' + v.title + '</a> <div class="listItemDescription">' + v.description + '</div> </li>');
        });    
        
    });

    //Show the modal
    $("#publicMapsModal").show();
    
}

//List all the maps of the current user
function listMyMaps(){

    //If the user is not logged in
    if (currentUser === null){

        alert("You must be logged in to access your Maps!");

    }

   else{

        //Get the maps from the DB
        getServerData("ws/services/maps/user/"+currentUser.id, function(listArray){


            //Empty the list
            $("#myMapsModalMapsList").empty();

            var myMapsArray = JSON.parse(JSON.stringify(listArray));
            
            $.each(myMapsArray ,function(i,v) {
                $("#myMapsModalMapsList").append('<li class="listItem" onclick="selectMap('+v.id+');"> <a >' + v.title + '</a> <div class="listItemDescription">' + v.description + '</div> </li>') ;
            });    
            
        });

        //Change the Title of the Modal
        $("#myMapsModalTitle").text("My Maps");

        //Show the modal
        $("#myMapsModal").show();
    
    }
    
}

//List all the maps of the current user
function listUserMaps(userId){
    
    //Get the maps from the DB
    getServerData("ws/services/maps/user/"+userId, function(listArray){    
        
        //Empty the list
        $("#myMapsModalMapsList").empty();
        
        var myMapsArray = JSON.parse(JSON.stringify(listArray));
        
        $.each(myMapsArray ,function(i,v) {
            $("#myMapsModalMapsList").append('<li class="listItem" onclick="selectMap('+v.id+');"> <a >' + v.title + '</a> <div class="listItemDescription">' + v.description + '</div> </li>') ;
        });    
        
    });
    
    //Hide all existing modal
    $(".modal").hide();    

    //Change the Title of the Modal
    getServerData("ws/services/users/search/"+userId, function(user){

        $("#myMapsModalTitle").text(user.username + " Maps");

    });

    //Show the modal
    $("#myMapsModal").show();
    
}

//jQuery (runs when page is loaded)
$(document).ready(function($) {

   //MyMaps button
    $("#myMapsModalCreateMapsButton").click(function(){
        
        cancelMapsModal();
        $("#createMapsModal").show();

    });
    $("#createMapsModalCancelButton").click(cancelCreateMapsModal);
    $("#closeCreateMapsModal").click(cancelCreateMapsModal);
    $("#createMapsModalCreateMapsButton").click(createMap);


    //myMaps list
    $("#myMapsButton").click(listMyMaps);
    $("#myMapsModalCancelButton").click(cancelMapsModal);
    $("#closeMyMapsModal").click(cancelMapsModal);


    //publicMaps list
    $("#publicMapsButton").click(listPublicMaps);
    $("#publicMapsModalCancelButton").click(cancelMapsModal);
    $("#closePublicMapsModal").click(cancelMapsModal);

    //editMap
    $("#editMapsModalEditMapsButton").click(editMap);
    $("#closeEditMapsModal").click(cancelMapsModal);
    $("#editMapsModalCancelButton").click(cancelMapsModal);



});