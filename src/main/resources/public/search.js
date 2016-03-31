console.log("Made by CWhy")
var frm = $('#search-form');
var inp = $('#search-form input');
// var btn = $('#search-btn');
function showResults(data){
    $('ul#hits li').remove();
    var res = $.parseJSON(data);
    var nhits = res.N_hits_found;
    $('ul#hits p').html("&#8680; " + nhits + " results found:");
    if (nhits !== 0){
        $.each(res.hits, function(i, h){
            var con = $("<li></li>").text(h.title);
            console.log(h);
            $('ul#hits').append(con);
        })
    }
}
frm.submit(function (ev) {
    $.ajax({
        url: 'search/all/' + inp.val(),
        success: showResults
    });
    ev.preventDefault();
});
