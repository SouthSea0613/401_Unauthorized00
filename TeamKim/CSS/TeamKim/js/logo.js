// 단일 요소 선택 시 querySelector 사용
let signupDiv = document.querySelector(".logo")

// 이벤트 리스너 추가
signupDiv.addEventListener("click", function () {
  location.href = "./signup_main.html" // 경로 확인 필요
})
