var inputElement = document.getElementById('inputFileToLoad');
var currentPicture = null;

inputElement.addEventListener("change", handleFiles, false);

function handleFiles() {

    var selectedFile = document.getElementById('inputFileToLoad').files;
    //Check File is not Empty
    if (selectedFile.length > 0) {
        // Select the very first file from list
        var fileToLoad = selectedFile[0];
        // FileReader function for read the file.
        var fileReader = new FileReader();
        var base64;
        // Onload of file read the file content
        fileReader.onload = function(fileLoadedEvent) {
           
            base64 = fileLoadedEvent.target.result;
            document.getElementById("photoPreview").src = base64;
            currentPicture = base64;
            
        };

        fileReader.readAsDataURL(fileToLoad);
            
    }
} 


//Open the photoList
function openPhotoList(selection){

    if (selection===null){

        alert("There are no pictures on the default Map");

    }

    else{
        
        
        $("#photoModalTitle").text("Pictures from: " + selection.title);
    
        //Get the photo from the DB
        getServerData("ws/services/pins/search/"+currentPin.id, function(pin){

            //Empty the list
            $("#photoModalPhotoList").empty();

            var myPhotoArray = JSON.parse(JSON.stringify(pin.images));
            
            $.each(myPhotoArray ,function(i,v) {
                $("#photoModalPhotoList").append('<div class="horizontalListItem"> <div class= "imageCont" ><img src="'+v+'" class="photoListItem"> </div> </div>');
            });    
    
        });

        $("#photoModal").show();

    }

}


function closeModal (){

    $(".modal").hide();    

}

$(document).ready(function($) {

    //Photo
    $("#photoButton").click(function(){

        if(currentPin==null){

            alert("There is no selected Pin!");

        }

        else{

            openPhotoList(currentPin);

        }

    });
    $("#closePhotoModal").click(closeModal);
    $("#photoModalCancelButton").click(closeModal);

    $("#photoModalAddPhotoButton").click(function(){

        document.getElementById("photoPreview").src = "";
        $("#photoModal").hide();
        $("#addPhotoModal").show();        

    });
    $("#closeAddPhotoModal").click(closeModal);
    $("#addPhotoModalCancelButton").click(closeModal);

    $("#addPhotoModalAddButton").click(function(){

        var myPicture = {

            pinId: currentPin.id,
            image: currentPicture

        }

        postServerData("ws/services/pins/images/add", JSON.stringify(myPicture), function(){

            currentPicture = null;
            $("#addPhotoModal").hide();    

            if (currentPin === null){
                alert("There is no selected Pin!");
            }

            else {
                openPhotoList(currentPin);
            }


        });


    });

});

