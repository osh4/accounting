$(document).ready(function () {
    $('#btn-add').click(function createSetting() {
        const group = $('#group').val();
        const key = $('#key').val();
        const type = $('#type').val();
        const value = $('#value').val();

        $.ajax({
            url: '/settings/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({group: group, key: key, type: type, value: value})
        });
        refreshSettingsList();
    });
});


function refreshSettingsList() {
    $.ajax({
        url: '/settings/async',
        type: 'GET',
        contentType: 'application/json'
    })
        .done(function (data) {
            alert("Data Loaded: " + data);
        });
}
