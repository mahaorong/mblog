clone_fun = function () {

}
var upload_image = {
    image_num: 0,
    can_upload: 1,
    onload: function () {
        var that = this;
        $("[data-imageupload]").on("click", function () {
            if (that.can_upload) {
                $("#upload_image").get(0).click();
            } else {
                alert("最多可上传四张图片");
            }
        })
        $("input[type='file']").on("change", function () {
            var file = this.files[0];
            //判断是否是图片类型
            if (!/image\/\w+/.test(file.type)) {
                alert("只能选择图片");
                return false;
            }
            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function (e) {
                $("<div class='upload_image_div' style='background-image: url(" + this.result + ")'><div onclick='upload_image.image_delete(this)'><span>+</span></div></div>").appendTo($("#upload_box"));
                $("#upload_box").addClass("upload_box");
                that.image_num++;
                if (that.image_num === 4) {
                    that.can_upload = 0;
                }
            }
        })
    },
    image_delete: function (a) {
        var that = this;
        $(a).parent().remove();
        that.image_num--;
        if (that.image_num === 4) {
            that.can_upload = 0;
        }
        else{
            that.can_upload = 1;
        }
        if (that.image_num <= 0) {
            $("#upload_box").removeClass("upload_box");
        }
    }
}


$(document).ready(function () {
    upload_image.onload();
});