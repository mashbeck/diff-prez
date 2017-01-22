/*
  Do these things when the page loads
*/
$(function()) {
  //TODO: FIND THINGS TO DO
  var form = document.getElementById('formID');
  form.noValidate = true;
  form.addEventListener('submit', function(event)) {
    if (!event.target.checkValidity()) {
      event.preventDefault();
      document.getElementById('errorMessageDiv').style.display = 'block';
    }

  }

});
