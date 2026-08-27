$("#title").on("input", function() {
    if ($(this).val().length >= 50) {
        $("#titleError").text("題名は50文字までです。");
    } else {
        $("#titleError").text("");
    }
});


$("#content").on("input", function() {
    if ($(this).val().length >= 2000) {
        $("#contentError").text("内容は2000文字までです。");
    } else {
        $("#contentError").text("");
    }
});