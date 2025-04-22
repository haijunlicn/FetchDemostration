/*$(document).ready(function() {
    let table = $('#testTable').DataTable({
        "paging": false,
        "searching": true,
        "info": false,
        "lengthChange": false,
        "pageLength": 5
    });
});
*/

document.addEventListener("DOMContentLoaded", function () {
    let table = new DataTable("#testTable", {
        paging: false,
        searching: true,
        info: false,
        lengthChange: false,
        pageLength: 5
    });
});

