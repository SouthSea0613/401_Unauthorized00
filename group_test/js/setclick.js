function setclick() {
    const fields = ["idset", "pwset", "pwsetcheck", "nameset", "nikname", "emailset", "phoneset", "bithset"];
    const [idset, pwset, pwcheck, name, nikname, email, phone, selecteddate] = fields.map(id => document.getElementById(id).value);
    const gender = document.querySelector('input[name="genderset"]:checked')?.value;

    // 필수 입력값 검증
    for (const [i, value] of fields.entries()) {
        if (!value) {
            alert(`${["아이디", "비밀번호", "비밀번호 확인", "성명", "닉네임", "이메일", "전화번호", "생년월일"][i]}을(를) 입력해주세요`);
            document.getElementById(fields[i]).focus();
            return;
        }
    }

    if (!gender) {
        alert("성별을 선택해주세요");
        return;
    }

    // 추가 조건 검증
    if (idset.length < 4 || idset.length > 10) return alert("아이디는 4~10자 사이여야 합니다");
    if (pwset.length < 8 || pwset.length > 14) return alert("비밀번호는 8~14자 사이여야 합니다");
    if (pwset !== pwcheck) return alert("비밀번호가 일치하지 않습니다");

    // 사용자 데이터 저장
    const usersArray = JSON.parse(localStorage.getItem("accounts")) || [];
    if (usersArray.some(user => user.idset === idset)) return alert("이미 존재하는 아이디입니다");

    usersArray.push({ idset, pwset, name, nikname, email, phone, selecteddate, gender });
    localStorage.setItem("accounts", JSON.stringify(usersArray));

    alert("회원가입 성공");
    location.reload();
}

function loaduserdata() {
    console.log("저장된 데이터", JSON.parse(localStorage.getItem("userdata")) || []);
}
