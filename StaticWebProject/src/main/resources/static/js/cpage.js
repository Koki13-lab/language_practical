$(document).ready(function() {
    $("#add-date").click(function() {

        let adddate = `
			<tr>
				<td></td>
				<td>
					<input type="checkbox">
				</td>
				<td>
					<input type="text">
				</td>
				<td>
					<button class="delete">削除</button>
				</td>
			</tr>
			`;
        $("#tbody").append(adddate);
		reNumbers();
    });
});

$("#tbody").on("click", ".delete", function() {
    $(this).closest("tr").remove();
	reNumbers();
});

function reNumbers(){
    $("#tbody tr").each(function(index) {
        $(this).find("td:first").text(index + 1);
    });
}