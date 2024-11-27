function loginclick() {
    let id = document.getElementById("id").value;
    let pw = document.getElementById("pw").value;

    if (id === "") {
        alert("아이디를 입력해주세요");
        document.getElementById("id").focus();
        return;
    }
    else if (id.length < 4 || id.length > 10) {
        alert("아이디의 길이가 충족되지않았습니다");
        document.getElementById("id").focus();
        return;
    }
    else if (pw === "") {
        alert("비밀번호를 입력해주세요");
        document.getElementById("pw").focus();
        return;
    }
    else if (pw.length < 8 || pw.length > 14) {
        alert("비밀번호의 길이가 충족되지않았습니다");
        document.getElementById("pw").focus();
        return;
    }
    else {
        let userdata = JSON.parse(localStorage.getItem('accounts') || []);
        if (userdata.find(user => user.idset === id && user.pwset === pw)) {
            location.href = "../html/home.html";
        }
        else {
            alert("아이디 또는 비밀번호가 일치하지 않습니다");
        }
    }
}
