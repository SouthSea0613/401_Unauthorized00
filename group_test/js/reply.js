let replyArr = [];
let profileImg = "";
let loginingId = localStorage.getItem("loginingId");
let accounts = JSON.parse(localStorage.getItem("accounts")) || [];

onload = function () {
    replyArr = JSON.parse(localStorage.getItem("ReplyArr")) || [];

    $(".reply").text("");
    for (let i = 0; i < replyArr.length; i++) {
        let div2 = $("<div></div>");
        let id = accounts.find(({idset}) => idset === replyArr[i].split(" : ")[0])
        div2.html(`<img src="${id.nikname}" width="50"> ${replyArr[i]}`);
        $(".reply").append(div2);
    }
    $("#intBox").val("");

    $(".img").text('');
    let img = localStorage.getItem('img')
    let contents = localStorage.getItem('contents')
    let IMG= $(`<img id="postimg" src=${img} alt="Post image" onclick="location.href='reply.html'">`)
    $(".img").prepend(IMG);
    let CONTENTS = $(`<p id="content_p">${contents}</p>`)
    $(".mainUser2").append(CONTENTS);
};

function input() {
    replyArr = JSON.parse(localStorage.getItem("ReplyArr")) || [];
    replyArr.push(`${loginingId} : ${$("#intBox").val()}`);

    $(".reply").text("");
    for (let i = 0; i < replyArr.length; i++) {
        let div2 = $("<div></div>");
        let id = accounts.find(({idset}) => idset === replyArr[i].split(" : ")[0])
        div2.html(`<img src="${id.nikname}" width="50"> ${replyArr[i]}`);
        $(".reply").append(div2);
    }
    $("#intBox").val("");
    localStorage.setItem("ReplyArr", JSON.stringify(replyArr));

    $(".img").text('');
    let img = localStorage.getItem('img')
    let contents = localStorage.getItem('contents')
    let IMG= $(`<img id="postimg" src=${img} alt="Post image" onclick="location.href='reply.html'">`)
    $(".img").prepend(IMG);
    let CONTENTS = $(`<p id="content_p">${contents}</p>`)
    $(".mainUser2").append(CONTENTS);
}

function follow(button) {
    if (button.value === "팔로우") {
        button.value = "팔로잉";
    } else {
        button.value = "팔로우";
    }
}