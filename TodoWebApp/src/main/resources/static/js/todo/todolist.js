$(function() {
    $(".sortBtn").on("click", function() {
        const button = $(this);
        const column = button.data("column");
        const currentState = button.text();
        let nextState;

        if (currentState === "-") {
            nextState = "▲";
        } else if (currentState === "▲") {
            nextState = "▼";
        } else {
            nextState = "▲";
        }

        $(".sortBtn").text("-");
        button.text(nextState);
        const rows = $("#todoTable tbody tr").get();

        rows.sort(function(a, b) {
            const valueA = $(a)
                .children("td")
                .eq(column)
                .text()
                .trim();
            const valueB = $(b)
                .children("td")
                .eq(column)
                .text()
                .trim();

            if (column === 0) {
                const numberA = Number(valueA);
                const numberB = Number(valueB);
                if (nextState === "▲") {
                    return numberA - numberB;
                }
                if (nextState === "▼") {
                    return numberB - numberA;
                }
            }
            if (nextState === "▲") {
                if (valueA < valueB) {
                    return -1;
                }
                if (valueA > valueB) {
                    return 1;
                }
            }
            if (nextState === "▼") {
                if (valueA < valueB) {
                    return 1;
                }
                if (valueA > valueB) {
                    return -1;
                }
            }
            return 0;
        });
        $("#todoTable tbody").append(rows);
    });

});