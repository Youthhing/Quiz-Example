import http from 'k6/http';
import { check } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.1.0/index.js';

export default function () {
    const url = 'http://localhost:8080/api/reply';

    const payload = JSON.stringify({
        quizId: 3, // 실제 테스트에서 필요한 값으로 변경하세요
        memberId: randomIntBetween(1, 100),
        answer: 3 // 실제 테스트에서 필요한 값으로 변경하세요
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'is status 200': (r) => r.status === 200,
    });
}
