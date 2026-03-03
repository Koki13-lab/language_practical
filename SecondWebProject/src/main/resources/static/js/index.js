document.getElementById("getUsers").addEventListener("click", () => {

    const keyword = document.getElementById("nameKeyword").value;

    const params = new URLSearchParams();

    if (keyword) {
        params.append("keyword", keyword);
    }
    fetch("http://localhost:8080/api/users?" + params.toString())
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById("tbody");
            tbody.innerHTML = "";
            data.forEach(users => {
                const tr = document.createElement("tr");
                const tdId = document.createElement("td");
                tdId.textContent = users.id;
                const tdName = document.createElement("td")
                tdName.textContent = users.name;
                const tdDelete = document.createElement("td");
                const btn = document.createElement("button");
                btn.textContent = "削除";
                btn.addEventListener("click", () => {
                    fetch(`http://localhost:8080/api/users/${users.id}`, {
                        method: "DELETE"
                    }).then(response => response.text()
                        .then(msg => {
                            if (response.ok) {
                                tr.remove();
                            }
                            document.getElementById("uresult").textContent = msg;
                        })
                    );
                });
                tdDelete.appendChild(btn);

                tr.appendChild(tdId)
                tr.appendChild(tdName);
                tr.appendChild(tdDelete);
                tbody.appendChild(tr);
            });
        })
});



document.getElementById("insertUsers").addEventListener("click", () => {

    const name = document.getElementById("userName").value;

    fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ name: name })
    })
        .then(response => {
            return response.text().then(msg => {
                document.getElementById("uresult").textContent = msg;

            })
        });
});

document.getElementById("getSkills").addEventListener("click", () => {

    const keyword = document.getElementById("skillKeyword").value;
    const sort = document.getElementById("skillSort").value;

    const params = new URLSearchParams();

    if (keyword) {
        params.append("keyword", keyword);
    }
    if (sort) {
        params.append("sort", sort);
    }

    fetch("http://localhost:8080/api/skills?" + params.toString())
        .then(response => response.json())
        .then(data => {
            const tbodyS = document.getElementById("tbodyS");
            tbodyS.innerHTML = "";
            data.forEach(skills => {
                const tr = document.createElement("tr");
                const tdName = document.createElement("td");
                tdName.textContent = skills.name;
                const tdSkill = document.createElement("td")
                tdSkill.textContent = skills.skill;
                const tdDelete = document.createElement("td");
                const btn = document.createElement("button");
                btn.textContent = "削除";
                btn.addEventListener("click", () => {
                    fetch(`http://localhost:8080/api/skills/${skills.id}`, {
                        method: "DELETE"
                    }).then(response => response.text()
                        .then(msg => {
                            if (response.ok) {
                                tr.remove();
                            }
                            document.getElementById("sresult").textContent = msg;
                        })
                    );
                });
                tdDelete.appendChild(btn);

                tr.appendChild(tdName);
                tr.appendChild(tdSkill);
                tr.appendChild(tdDelete);
                tbodyS.appendChild(tr);
            });
        })
});

document.getElementById("insertSkill").addEventListener("click", () => {

    const userid = document.getElementById("userId").value;
    const skill = document.getElementById("userSkill").value;
    fetch("http://localhost:8080/api/skills", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ userid: userid, skill: skill })

    })
        .then(response => {
            return response.text().then(msg => {
                document.getElementById("sresult").textContent = msg;
            })

        });
})

document.querySelectorAll("button").forEach(btn => {
    btn.addEventListener("click", () => {
        document.querySelectorAll(".result").forEach(div => {
            div.textContent = "";
        });
        document.querySelectorAll(".input").forEach(input => {
            input.value = "";
        });
    })
})