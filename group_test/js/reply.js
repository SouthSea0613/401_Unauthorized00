let replyArr = [];
let profileImg = "../jQuery/스크린샷 2024-10-10 143633.png";

onload = function () {
    replyArr = JSON.parse(localStorage.getItem("ReplyArr")) || [];
    replyArr.push($("#intBox").val());

    $(".reply").text("");
    for (let i = 0; i < replyArr.length; i++) {
        let div2 = $("<div></div>");
        div2.html(`<img src="${profileImg}" width="50"> ${replyArr[i]}`);
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
    replyArr.push($("#intBox").val());

    $(".reply").text("");
    for (let i = 0; i < replyArr.length; i++) {
        let div2 = $("<div></div>");
        div2.html(`<img src="${profileImg}" width="50"> ${replyArr[i]}`);
        $(".reply").append(div2);
    }
    $("#intBox").val("");

    localStorage.setItem("ReplyArr", JSON.stringify(replyArr));
}

function follow(button) {
    if (button.value === "팔로우") {
        button.value = "팔로잉";
    } else {
        button.value = "팔로우";
    }
}