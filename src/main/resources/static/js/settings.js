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
        .done(function (json) {
            const settingsTable = $('.js-settings-table .js-table-body');
            let content = '';
            if (json === undefined || json.length === 0) {
                content = '<tr><td colSpan="5"> No Settings provided yet</td></tr>';
            } else {
                for (let i = 0; i < json.length; i++) {
                    content += '<tr>';
                    content += '<td><span>' + json[i].group + '</span></td>';
                    content += '<td><span>' + json[i].key + '</span></td>';
                    content += '<td><span>' + json[i].type + '</span></td>';
                    content += '<td><span>' + json[i].value + '</span></td>';
                    content += '<td><button id="remove">X</button></td>';
                    content += '</tr>';
                }
            }

            settingsTable.html(content);
            alert("Data Loaded successfully!");
        });
}
