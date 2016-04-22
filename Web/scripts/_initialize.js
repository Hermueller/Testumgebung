$(function () {
    $("#tabs").tabs({
        heightStyle: "fill"
    });
    $("#accordion_Project-Manager").accordion({
        collapsible: true,
        heightStyle: "content",
        active: false
    });
    $("#accordion_Team-Members").accordion({
        collapsible: true,
        heightStyle: "content",
        active: false
    });
});