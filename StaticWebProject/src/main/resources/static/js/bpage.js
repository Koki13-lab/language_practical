$(function() {
    $('#text-form-submit').on('click', function() {
        let textformresult = $('#text-form').val()
        $('#text-form-result').text(textformresult)
    })
});

$(function() {
    $('#textarea-submit').on('click', function() {
        let textarearesult = $('#textarea').val()
        $('#textarea-result').text(textarearesult)
    })
});

$(function() {
    $('#radio-submit').on('click', function() {
        let radioresult = $('input[name="radio"]:checked').val()
        $('#radio-result').text(radioresult)
    })
});

$(function() {
    $('#checkbox-submit').on('click', function() {
        $('#checkbox-result').empty();
        $('input[name="checkbox"]:checked').each(function() {
            $('#checkbox-result').append('<div>' + $(this).val() + '</div>');
        });
    });
});

$(function() {
    $('#selectbox-submit').on('click', function() {
        let selectboxresult = $('#selectbox').val()
        $('#selectbox-result').text(selectboxresult)
    })
});