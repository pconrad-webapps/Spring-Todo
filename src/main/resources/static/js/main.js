console.log("hello there");

$('button[name="deleteItem"]').click(function (event) {
  console.log(event);
  var id = this.dataset.id
  $.post('delete/' + id, function (data) {
    location.reload();
  })
});