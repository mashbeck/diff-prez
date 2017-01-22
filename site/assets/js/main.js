/*
  Do these things when the page loads
*/
$(function()) {
  //TODO: FIND THINGS TO DO
  var forms = document.getElementsByTagName('formID');
  for (var i = 0; i < forms.length; i++) {
    forms[i].noValidate = true;
    forms[i].addEventListener('submit', function(event) {
      if (!event.target.checkValidity()) {
        event.preventDefault();
      }
    }, false);
  }
});
