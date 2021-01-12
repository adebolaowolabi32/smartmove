let seatNumber=[];
function seatBooking(id){

 let elements = document.querySelectorAll(".seatLabel");
 let inputField= document.getElementById('captureData');
 let submitButton= document.getElementById('seatButton');

  for (let elem of elements) {
      if(elem.id===id){

       if(elem.style.background==='red'){
        return this;
       }
       if(elem.style.background==='white'){
             seatNumber.push(id)
             elem.style.background='#74C965';
             let seatData=seatNumber.join();
             inputField.value=seatData;

        }else{

           if(elem.style.background==='none'){

           elem.style.background='#74C965';
           }else{
            elem.style.background='white';
           }

             //loop through the array and search for the id:
             try{
                for(let i=0;i<seatNumber.length;i++){

                if(seatNumber[i]===id){
                         seatNumber.splice(i,1);
                         let seatData=seatNumber.join();
                         inputField.value=seatData;
                }

                 }
             }catch(e){
             console.error(e)
             }

        }

        //validate the submit button.
        if(seatNumber.length>0){
        submitButton.disabled=false;
        }else{
        submitButton.disabled=true;
        }

}
}

console.log("SeatNumberSize===>"+seatNumber.length)
}

