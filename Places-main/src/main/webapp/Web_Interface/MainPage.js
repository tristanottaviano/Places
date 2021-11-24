var displayMap;
var directionsService;
var directionsRenderer;
var placingPin = false;
var placingDirectionOrigin = false;
var currentDirectionPin = null;
var currentPin = null;
var createdPin = null;
var currentMap = null;
var currentUser = null;

var displayMarkers = [];

const MAP_BOUNDS = {
    north: 85,
    south: -85,
    west: -180,
    east: 180,
};


function getServerData(url, success){
    $.ajax({
        dataType: "json",
        url: url
    }).done(success);
}

function postServerData(url, data, success){
    $.ajax({
        type: 'POST',
        url: url,
        data: data,
        contentType : 'application/json',
        dataType: "json"
    }).done(success);
}

function putServerData(url, data, success){
    $.ajax({
        type: 'PUT',
        url: url, 
        data: data,
        contentType : 'application/json',
        dataType: "json"
    }).done(success);
}

function putSuccessFunction(result){

    console.log(result);

}

function initMap() {
    
    //Initializing the map
    displayMap = new google.maps.Map(document.getElementById("map"), {
        draggableCursor:'default',
        center: { lat: 0, lng:0},
        zoom: 3,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.BOTTOM_RIGHT
        },
        fullscreenControlOptions: {
            position: google.maps.ControlPosition.BOTTOM_RIGHT
        },
        restriction: {
            latLngBounds: MAP_BOUNDS,
            strictBounds: true  
        }
        
    });

    //Adding the direction Services
    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer();
    directionsRenderer.setMap(displayMap);

    //Adding a listener (cancel action)
    google.maps.event.addListener(displayMap, 'rightclick', function(e) {cancelAction();
    });
    
    //Adding a listener (place a pin)
    google.maps.event.addListener(displayMap, 'click', function(e) {clickOnMap(e.latLng);
    });

}

function clickOnMap(location) {

    //If placing a pin
    if (placingPin == true){
        
        //Set marker icon
        var icon = {
            url: "./Resource/Marker.png",
            scaledSize: new google.maps.Size(35, 35)
        };
        
        
        //Place the marker
        var marker = new google.maps.Marker({
            position: location, 
            map: displayMap,
            icon: icon,
        });
        
        //Adding a Listener
        marker.addListener('click', function (e){ selectPin(marker);
        });
        
        //Setting the created Pin to the maker
        createdPin = marker;
        
        //Reset the cursor to drag
        displayMap.setOptions({draggableCursor:'default'});
        
        //Open up the modal window
        $("#createPinModal").show();
        
        //Reset placingPin
        placingPin = false;
        
    }
    
    else if(placingDirectionOrigin == true && currentPin!=null){
        
        //If there is already a directionOrigin marker, we remove it
        if (currentDirectionPin != null){
            
            currentDirectionPin.setMap(null);
            
        }
        
        //Placing the new marker
        var marker = new google.maps.Marker({
            position: location, 
            map: displayMap,
            icon: null,
        });
        
        //Setting the created Pin to the maker
        currentDirectionPin = marker;
        
        //Show the direction
        directionsRenderer.setMap(displayMap);
        
        //Initializing the travelMode variable
        var travelMode = $("input[name='directionType']:checked").val(); 
        
        //Creating a request to draw the best route
        var request = {
            origin: new google.maps.LatLng(currentPin.position.lat(), currentPin.position.lng()),
            destination: new google.maps.LatLng(marker.position.lat(), marker.position.lng()),
            travelMode: travelMode
        };
        
        
        directionsService.route(request, function(response, status){
            
            if (status == 'OK'){
                
                directionsRenderer.setDirections(response);
                
            }
            
        });
        
        
    }
    
    //Unselect currentPin
    else if(currentPin!=null){
        
        /* $("#findWayButton").hide();
        $("#photoButton").hide();
        $("#directionTypeCont").hide();
         */
        unselectPin(currentPin);
        refreshCurrentPinPanel(currentMap);
        
    }
    
}

function createPin(){

    //Get the values inputed by the user
    var title = $("#creatPinModalName").val();
    var description = $("#creatPinModalDescription").val();
    var tags = $("#creatPinModalTags").val();

    if (title == ""){

        alert("You must add a valid Title!");

    }

    else if(title.length > 25){

        alert("Your Title is too long! (25 characters max.)");

    }
    
    else if(description.length > 200){

        alert("Your Description is too long! (200 character max.)");

    }

    else{
        
        //Hide the modal window
        $("#createPinModal").hide();
        
        
        //Clear the textBoxes
        $("#creatPinModalName").val('');
        $("#creatPinModalDescription").val('');
        $("#creatPinModalTags").val('');
        
        //Add the marker to the list
        displayMarkers.push(createdPin);

        //Creating a new object to send
        var myPin = {
            
            id: 0,
            title: title,
            description: description,
            tags: tags,
            latitude: createdPin.position.lat(),
            longitude: createdPin.position.lng(),
            map: currentMap.id,
            images: null,
            
        };
        
        //Send the pin WS
        postServerData("ws/services/pins/create", JSON.stringify(myPin), function(pin){
            
            //Add properties to the pin
            createdPin["title"] = title;
            createdPin["description"] = description; 
            createdPin["tags"] = tags;
            createdPin["id"] = pin.id;

            //Set createdPin back to null
            createdPin = null;

        });
        
    }
    
}

function editPin(){

    //Get the values inputed by the user
    var title = $("#editPinModalName").val();
    var description = $("#editPinModalDescription").val();
    var tags = $("#editPinModalTags").val();


    if (title == ""){

        alert("You must add a valid Title!");

    }

    else if(title.length > 25){

        alert("Your Title is too long! (25 characters max.)");

    }
    
    else if(description.length > 200){

        alert("Your Description is too long! (200 character max.)");

    }

    else{
        
        //Hide the modal window
        $("#editPinModal").hide();
        
        //Add properties to the pin
        currentPin.title = title;
        currentPin.description = description; 
        currentPin.tags = tags;
        
        //Clear the textBoxes
        $("#editPinModalName").val('');
        $("#editPinModalDescription").val('');
        $("#editPinModalTags").val('');
        
        //Creating a new object to send
        var myPin = {

            id: currentPin.id,
            title: title,
            description: description,
            tags: tags,
            latitude: currentPin.position.lat(),
            longitude: currentPin.position.lng(),
            map: currentMap.id,
            images: currentPin.images

        };
        
        //Send the pin WS
        postServerData("ws/services/pins/edit", JSON.stringify(myPin), refreshCurrentPinPanel);
    
    }

}


function refreshCurrentPinPanel(selection){

    if (selection === null){

        $("#findWayButton").hide();
        $("#photoButton").hide();
        $("#directionTypeCont").hide();
        $("#selectionTitle").text("Default Map");
        $("#selectionDescription").text("Default Description");
        $("#selectionTags").text("Default Tags");

    }

    else{

        if (currentPin!=null){

            $("#findWayButton").show();
            $("#photoButton").show();
            $("#directionTypeCont").show();

        }
        
        $("#selectionTitle").text(selection.title);
        $("#selectionDescription").text(selection.description);
        $("#selectionTags").text(selection.tags);

    }

}

function refreshCurrentUserPanel(user){

    if (user === null){

        $("#leftNavContUserPanelUsername").text("Not logged in");
        $("#leftNavContUserPanelBio").text("");

    }

    else {

        $("#leftNavContUserPanelUsername").text("Logged in as " + user.username);
        $("#leftNavContUserPanelBio").text(user.bio);

    }


}

function selectPin(pin){
   
    //Initializing marker icons
    var selectIcon = {
        url: "./Resource/Marker.png",
        scaledSize: new google.maps.Size(50, 50)
    };
    
    
    //If the pin is null, there is a probleme
    if (pin==null){
        
        alert("The pin is NULL");
        
    }   
    
    //If the pin is alread selected, we unselect it
    else if (pin === currentPin){
        
        unselectPin(pin);
        refreshCurrentPinPanel(currentMap);
        
    }    
    
    else {
        
        //Unselect any ppotential currentPin
        unselectPin(currentPin);
        
        //Select the new Pin
        pin.setIcon(selectIcon);
        currentPin = pin;
        
        //Get the pin from WS
        getServerData("ws/services/pins/search/"+pin.id, refreshCurrentPinPanel);

    }
    
}

function unselectPin(pin){
    
    $("#findWayButton").hide();
    $("#directionTypeCont").hide();
    $("#photoButton").hide();
    
    if(pin != null){
        
        var unselectedIcon = {
            url: "./Resource/Marker.png",
            scaledSize: new google.maps.Size(35, 35)
        };
        
        pin.setIcon(unselectedIcon);
        currentPin = null;

    }

}

function cancelAction(){

    //Reset the cursor to drag
    displayMap.setOptions({draggableCursor:'default'});

    //Reseting every action
    placingPin = false;
    placingDirectionOrigin = false;
    
    directionsRenderer.setMap(null);
    
    if (currentDirectionPin != null){

        currentDirectionPin.setMap(null);
        currentDirectionPin = null;

    }

}

function addPin(){
    
    if (currentMap === null){

        alert("You can't add a pin to the default map");

    }

    else if (currentMap.user != currentUser.id){

        alert ("You can't add a pin on somebody elses map");

    }

    else{

        //Allowing the user to place a pin
        placingPin = true;
        
        //Set the cursor to pin
        displayMap.setOptions({draggableCursor:'crosshair'});

    }
}

function deletePin(){

    //If the pin is not NULL, remove it from the map
    if(currentPin!=null){

        //Delete the pin from the data base
        getServerData("ws/services/pins/delete/"+currentPin.id, function(){

            unselectPin(currentPin);

            getServerData("ws/services/maps/search/"+currentMap.id, function(map){

                currentMap = map;
                refreshCurrentPinPanel(map);
                refreshMarkers(map);

            });
        });
        
            
    }

    else if(createdPin!= null){

        createdPin.marker.setMap(null);
        unselectPin(createdPin);
        createdPin = null;

    }

}

//Close the modal window
function cancelCreatePinModal() {
    
    $(".modal").hide();

    //Clear the textBoxes
    $("#creatPinModalName").val('');
    $("#creatPinModalDescription").val('');
    $("#creatPinModalTags").val('');

    deletePin();
    createdPin = null;

} 

//Close the modal window
function cancelEditPinModal() {
    
    $(".modal").hide();

    //Clear the textBoxes
    $("#editPinModalName").val('');
    $("#editPinModalDescription").val('');
    $("#editPinModalTags").val('');

} 

//Show all the buttons accecible if the user is connected
function showConnectedButtons(){

    $("#addPinItem").show();
    $("#deletePinButton").show();
    $("#commentPinButton").show();
    $("#photoButton").show();
    $("#editPinButton").show();
    $("#myFriendsItem").show();
    $("#myMapsItem").show();
    $("#requestItem").show();
    $("#logoutButtonCont").show();

}

//Hide all the buttons accecible if the user is connected
function hideConnectedButtons(){

    $("#addPinItem").hide();
    $("#deletePinButton").hide();
    $("#commentPinButton").hide();
    $("#editPinButton").hide();
    $("#myFriendsItem").hide();
    $("#myMapsItem").hide();
    $("#requestItem").hide();
    $("#logoutButtonCont").hide();
    $("#findWayButton").hide();
    $("#photoButton").hide();
    $("#directionTypeCont").hide();

}

//jQuery (runs when page is loaded)
$(document).ready(function($) {

    //Hide all the unecesary button (user not connected)
    hideConnectedButtons();

    $("#addPinButton").click(addPin);
    $("#commentPinButton").click(function(){

        if (currentMap === null){

            alert("You can't comment the default map!");
    
        }

        else if(currentUser===null){

            alert("You must be logged in to comment a map!");

        }

        else {

            listComment();

        }

    });

    $("#deletePinButton").click(function (){


        if (currentMap === null){

            alert("You can't delete the default map");
    
        }
        

        else if (currentUser=== null){

            alert("You must be logged");

        }

        else if (currentMap.user != currentUser.id) {

            alert("You can't delete anything on somebody else's map!");

        }

        else if(currentPin != null || createdPin!= null){
            
            deletePin();

        }

        else {

            deleteMap();

        }
        
    });

    $("#closeCreatPinModal").click(cancelCreatePinModal);
    $("#creatPinModalCancelButton").click(cancelCreatePinModal);
    $("#creatPinModalCreatePinButton").click(createPin);

    //Edit pin
    $("#editPinButton").click(function(){


        if (currentMap.user != currentUser.id) {

            alert("You can't edit anything on somebody else's map!");


        }

        else if (currentPin == null){

            if (currentMap === null){

                alert("You can't edit the default map");
        
            }
        
            else {

                $("#editMapsModalTitle").val(currentMap.title);
                $("#editMapsModalDescription").val(currentMap.description);
                $("#editMapsModalTags").val(currentMap.tags);
                if (currentMap.status === 1)  $("#editMapsModalPublicSlider").prop('checked', true);
                else $("#editMapsModalPublicSlider").prop('checked', false);
                
                $("#editMapsModal").show();

            }

        }

        else {

            $("#editPinModalName").val(currentPin.title);
            $("#editPinModalDescription").val(currentPin.description);
            $("#editPinModalTags").val(currentPin.tags);
            $("#editPinModal").show();

        }
    
    });
    $("#editPinModalCancelButton").click(cancelEditPinModal);
    $("#closeEditPinModal").click(cancelEditPinModal);
    $("#editPinModalEditPinButton").click(editPin);

    //Find Way
    $("#findWayButton").click(function(){

        //Allowing the user to place a depature point
        placingDirectionOrigin = true;
        
        //Set the cursor to pin
        displayMap.setOptions({draggableCursor:'crosshair'});

    });

    //Create user
    $("#createUserButton").click(function(){
        
        $("#createUserModal").show();

    });

});

