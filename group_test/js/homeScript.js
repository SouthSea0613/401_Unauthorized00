$(".commit-button").click(()=> {
    let input1 = $(".post input").eq(0).val();
    let input2 = $(".post input").eq(1).val();

    if (input1 || input2) {
        const post = $(`
            <div class="post">
                <img src="${input1}" alt="Post image" onclick="location.href='reply.html'">
                <p class="post-description">${input2}</p>
            </div>
        `);

        $(".post-section").prepend(post);

        // 입력 필드 초기화
        $(".post input").eq(0).val("");
        $(".post input").eq(1).val("");
    }
    else {
        alert("입력값이 없습니다. 내용을 입력해주세요.");
    }
});

$($(".post input")[1]).change(()=> {
    let input1 = $(".post input").eq(0).val();
    let input2 = $(".post input").eq(1).val();

    if (input1 || input2) {
        const post = $(`
            <div class="post">
                <img src="${input1}" alt="Post image" onclick="location.href='reply.html'">
                <p class="post-description">${input2}</p>
            </div>
        `);

        $(".post-section").prepend(post);

        // 입력 필드 초기화
        $(".post input").eq(0).val("");
        $(".post input").eq(1).val("");
    }
    else {
        alert("입력값이 없습니다. 내용을 입력해주세요.");
    }
});

console.log(localStorage.getItem("loginingId") || []);
