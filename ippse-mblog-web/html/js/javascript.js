//封装一个限制字数方法
var checkStrLengths = function (str,maxLength) {
    var maxLength = maxLength;
    var result = 0;
    if (str && str.length > maxLength) {
        result = maxLength;
    } else {
        result = str.length;
    }
    return result;
};

var clone_fun = function(){
}

//监听输入
$(".wishContent").on('input propertychange', function () {

    //获取输入内容
    var userDesc = $(this).val();

    //判断字数
    var len;
    if (userDesc) {
        len = checkStrLengths(userDesc, 99);
    } else {
        len = 0
    }

    //显示字数
    $(".wordsNum").html(len + '/99');
});


 //上传图片
 var inputlabelBox = document.querySelector('.inputlabelBox');
 var my_file = document.querySelector('#my_file');
 var img = document.querySelector('#img');

 my_file.onchange = function()
 {
     var file = this.files[0];
     var reader = new FileReader();
     reader.readAsDataURL(file);

     reader.onload = function()
     {
         img.src = this.result;
     }
 };

var inputlabelBox = document.querySelector('.inputlabelBox');
var my_file = document.querySelector('#my_files');
var img = document.querySelector('#imgd');

my_file.onchange = function()
{
    var file = this.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = function()
    {
        img.src = this.result;
    }
};
$('#exampleModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var recipient = button.data('whatever') // Extract info from data-* attributes
    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
    var modal = $(this)
    modal.find('.modal-title').text('New message to ' + recipient)
    modal.find('.modal-body input').val(recipient)
});


