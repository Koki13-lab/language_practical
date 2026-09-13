$("#name").on("input", function() {
    if ($(this).val().length >= 50) {
        $("#nameError").text("名前は50文字までです。");
    } else {
        $("#nameError").text("");
    }
});

$("#password").on("input", function() {
    if ($(this).val().length >= 255) {
        $("#passwordError").text("パスワードは255文字までです。");
    } else {
        $("#passwordError").text("");
    }
});

$("#mail").on("input", function() {
    if ($(this).val().length >= 256) {
        $("#mailError").text("メールアドレスは256文字までです。");
    } else {
        $("#mailError").text("");
    }
});

$("#remarks").on("input", function() {
    if ($(this).val().length >= 2000) {
        $("#remarksError").text("備考は2000文字までです。");
    } else {
        $("#remarksError").text("");
    }
});