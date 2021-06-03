var url = contextRoot + "tasks";
var http = new XMLHttpRequest();

function getTasks() {
    http.onreadystatechange = function() {
        if (this.readyState != 4 || this.status != 200) {
            return
        };

        var listOfTasks = JSON.parse(this.responseText);
        console.log(task);
        for (i = 0; i < listOfTasks.length; i++) {
            addTaskToList(listOfTasks[i].name);
        };
    };

    http1.open("GET", url)
    http1.send()
}

function createNewTask() {
    var task = {
        name: document.querySelector("#name").value
    };

    http.open("POST", url, true);
    http.setRequestHeader('Content-Type', 'application/json');
    http.onreadystatechange = function () {
        if (this.readyState !== 4 || this.status !== 200) {
            return;
        }

        addTaskToList(JSON.parse(http.responseText));
    };

    http.send(JSON.stringify(task));
    document.getElementById("name").value = "";
};


function addTaskToList(task) {
    var li = document.createElement("li");
    li.appendChild(document.createTextNode(task.name));
    document.querySelector("#tasks").appendChild(li);
};