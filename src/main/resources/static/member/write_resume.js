document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const cityTabs = document.querySelectorAll('.city-tab');
    const districtsContainer = document.getElementById('districts');
    const selectedRegionsContainer = document.getElementById('selected-regions');
    const desiredLocationList = document.getElementById('desiredLocationList');
    const maxSelection = 3;
    let selectedRegions = [];

    const cityDistricts = {
        '서울': ['강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '마포구', '서대문구', '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구'],
        '부산': ['강서구', '금정구', '기장군', '남구', '동구', '동래구', '부산진구', '북구', '사상구', '사하구', '서구', '수영구', '연제구', '영도구', '중구', '해운대구'],
        '대구': ['남구', '달서구', '달성군', '동구', '북구', '서구', '수성구', '중구'],
        '인천': ['강화군', '계양구', '남동구', '동구', '미추홀구', '부평구', '서구', '연수구', '옹진군', '중구'],
        '광주': ['광산구', '남구', '동구', '북구', '서구'],
        '대전': ['대덕구', '동구', '서구', '유성구', '중구'],
        '울산': ['남구', '동구', '북구', '울주군', '중구'],
        '세종': ['세종시'],
        '경기도': ['가평군', '고양시 덕양구', '고양시 일산동구', '고양시 일산서구', '과천시', '광명시', '광주시', '구리시', '군포시', '김포시', '남양주시', '동두천시', '부천시', '성남시 분당구', '성남시 수정구', '성남시 중원구', '수원시 권선구', '수원시 영통구', '수원시 장안구', '수원시 팔달구', '시흥시', '안산시 단원구', '안산시 상록구', '안성시', '안양시 동안구', '안양시 만안구', '양주시', '양평군', '여주시', '연천군', '오산시', '용인시 기흥구', '용인시 수지구', '용인시 처인구', '의왕시', '의정부시', '이천시', '파주시', '평택시', '포천시', '하남시', '화성시'],
        '강원도': ['강릉시', '고성군', '동해시', '삼척시', '속초시', '양구군', '양양군', '영월군', '원주시', '인제군', '정선군', '철원군', '춘천시', '태백시', '평창군', '홍천군', '화천군', '횡성군'],
        '충청북도': ['괴산군', '단양군', '보은군', '영동군', '옥천군', '음성군', '제천시', '증평군', '진천군', '청주시 상당구', '청주시 서원구', '청주시 청원구', '청주시 흥덕구', '충주시'],
        '충청남도': ['계룡시', '공주시', '금산군', '논산시', '당진시', '보령시', '부여군', '서산시', '서천군', '아산시', '예산군', '천안시 동남구', '천안시 서북구', '청양군', '태안군', '홍성군'],
        '전라북도': ['고창군', '군산시', '김제시', '남원시', '무주군', '부안군', '순창군', '완주군', '익산시', '임실군', '장수군', '전주시 덕진구', '전주시 완산구', '정읍시', '진안군'],
        '전라남도': ['강진군', '고흥군', '곡성군', '광양시', '구례군', '나주시', '담양군', '목포시', '무안군', '보성군', '순천시', '신안군', '여수시', '영광군', '영암군', '완도군', '장성군', '장흥군', '진도군', '함평군', '해남군', '화순군'],
        '경상북도': ['경산시', '경주시', '고령군', '구미시', '군위군', '김천시', '문경시', '봉화군', '상주시', '성주군', '안동시', '영덕군', '영양군', '영주시', '영천시', '예천군', '울릉군', '울진군', '의성군', '청도군', '청송군', '칠곡군', '포항시 남구', '포항시 북구'],
        '경상남도': ['거제시', '거창군', '고성군', '김해시', '남해군', '밀양시', '사천시', '산청군', '양산시', '의령군', '진주시', '창녕군', '창원시 마산합포구', '창원시 마산회원구', '창원시 성산구', '창원시 의창구', '창원시 진해구', '통영시', '하동군', '함안군', '함양군', '합천군'],
        '제주도': ['서귀포시', '제주시']
    };

    cityTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const city = tab.getAttribute('data-city');
            showDistricts(city);
            cityTabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
        });
    });

    const showDistricts = (city) => {
        const districts = cityDistricts[city];
        districtsContainer.innerHTML = '';
        districts.forEach(district => {
            const label = document.createElement('label');
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.value = district;
            checkbox.dataset.city = city;
            checkbox.checked = selectedRegions.some(region => region.district === district && region.city === city);
            checkbox.addEventListener('change', handleDistrictChange);
            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(district));
            districtsContainer.appendChild(label);
        });
    };

    const handleDistrictChange = (e) => {
        const district = e.target.value;
        const city = e.target.dataset.city;
        if (e.target.checked) {
            if (selectedRegions.length < maxSelection) {
                selectedRegions.push({ city, district });
                updateSelectedRegions();
            } else {
                e.target.checked = false;
                alert('최대 3개까지 선택 가능합니다.');
            }
        } else {
            selectedRegions = selectedRegions.filter(region => !(region.city === city && region.district === district));
            updateSelectedRegions();
        }
    };

    const updateSelectedRegions = () => {
        selectedRegionsContainer.innerHTML = '';
        selectedRegions.forEach(region => {
            const span = document.createElement('span');
            span.classList.add('selected-region');
            span.textContent = `${region.city} ${region.district}`;
            const removeBtn = document.createElement('span');
            removeBtn.classList.add('remove');
            removeBtn.textContent = 'x';
            removeBtn.addEventListener('click', () => {
                selectedRegions = selectedRegions.filter(r => !(r.city === region.city && r.district === region.district));
                updateSelectedRegions();
                showDistricts(document.querySelector('.city-tab.active').getAttribute('data-city'));
            });
            span.appendChild(removeBtn);
            selectedRegionsContainer.appendChild(span);
        });
        const locationStrings = selectedRegions.map(region => `${region.city} ${region.district}`);
        desiredLocationList.value = JSON.stringify(locationStrings);
    };

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const formObject = {};

        formData.forEach((value, key) => {
            if (value !== "" && value !== "on") { // 빈 문자열 및 체크박스 'on' 제거
                formObject[key] = value;
            }
        });

        // 체크박스 상태를 true/false로 설정
        const checkboxes = form.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            formObject[checkbox.name] = checkbox.checked;
        });

       try {
            formObject['desiredLocationList'] = JSON.parse(desiredLocationList.value);
        } catch (error) {
            console.error('Error parsing desiredLocationList:', error);
            return;
        }

        // 체크박스 값 처리
        const sanitizedObject = {};
        for (const key in formObject) {
            if (formObject.hasOwnProperty(key) && key.trim() !== "") {
                sanitizedObject[key] = formObject[key];
            }
        }

        $.ajax({
            url: '/member/write_resume',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(sanitizedObject),
            success: function(response){
                console.log("서버로 데이터 전달 성공");
                window.location.href = '/member/write_resume';
            },
            error: function(xhr, status, error) {
                console.error('서버 데이터 전달 실패:', error);
            }
        });
    });
});