import http from 'k6/http';
import { check } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.1.0/index.js';

export default function () {
    const url = 'http://localhost:8080/api/reply';

    const payload = JSON.stringify({
        quizId: 1, // 실제 테스트에서 필요한 값으로 변경하세요
        memberId: randomIntBetween(0, 40),
        answer: 1 // 실제 테스트에서 필요한 값으로 변경하세요
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'is status 200': (r) => r.status === 200,
        // 필요한 다른 체크 조건들을 추가하세요
        'is Response : ': (r) => r.response.result === true
    });
}
