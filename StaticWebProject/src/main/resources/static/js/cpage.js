let count = 0;

$(document).ready(function() {
    $("#add-date").click(function() {

        count++;

        let adddate = `
			<tr>
				<td>${count}</td>
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
    });
});

$("#tbody").on("click", ".delete", function() {
    $(this).closest("tr").remove();
});