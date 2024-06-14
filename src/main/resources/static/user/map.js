let markers = [];

// 지도 초기화 함수
function initMap() {
    // 사용자의 현재 위치 가져오기
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        defaultPosition();
    }
}

// 위치 가져오기 성공 함수
function showPosition(position) {
    var lat = position.coords.latitude;
    var lng = position.coords.longitude;

    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(lat, lng),
        zoom: 15,
        maxZoom: 17,
        minZoom: 8,
        zoomControl: true,
        zoomControlOptions: {
            position: naver.maps.Position.TOP_RIGHT
        }
    });

    getStore(map);
    addDragEventListener(map);
}

// 기본 위치
function defaultPosition() {
    var lat = 37.3595704;
    var lng = 127.105399;

    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(lat, lng),
        zoom: 15,
        maxZoom: 18,
        minZoom: 8,
        zoomControl: true,
        zoomControlOptions: {
            position: naver.maps.Position.TOP_RIGHT
        }
    });

    getStore(map);
    addDragEventListener(map);
}

// 위치 가져오기 실패 콜백 함수
function showError(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            defaultPosition();
            break;
        case error.POSITION_UNAVAILABLE:
            alert("Location information is unavailable.");
            break;
        case error.TIMEOUT:
            alert("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
            alert("An unknown error occurred.");
            break;
    }
}

function getStore(map) {
    var bounds = map.getBounds();
    var ne = bounds.getNE();
    var sw = bounds.getSW();

    $.ajax({
        url: '/getStore',
        method: 'GET',
        contentType: 'application/json',
        data: {
            northEastLat: ne.lat(),
            northEastLng: ne.lng(),
            southWestLat: sw.lat(),
            southWestLng: sw.lng()
        },
        success: function(response) {
            console.log('get Store from backend :', response);
            clearMarkers();
             clearStoreList();
            response.forEach(store => {
                var marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(store.area_lat, store.area_lng),
                    map: map
                });
                markers.push(marker);
                appendStoreToList(store);
            });
        },
        error: function(xhr, status, error) {
            console.error('백엔드로 좌표 전달 실패:', error);
        }
    });
}

function addDragEventListener(map) {
    naver.maps.Event.addListener(map, "dragend", () => {
        getStore(map);
    });
}

function clearMarkers() {
    markers.forEach(marker => marker.setMap(null));
    markers = [];
}

function clearStoreList() {
    $('#store-list').empty();
}

function appendStoreToList(store) {
    const storeItem = `
        <div class="store-item" style="cursor: pointer;" onclick="goToDetail(${store.store_id})" onmouseover="changeBackgroundColor(this, true)" onmouseout="changeBackgroundColor(this, false)">
            <img src="${'/resources/static/images/temp_store_logo.png'}" alt="${store.store_name}" style="width: 100%; height: auto;">
            <h4>${store.store_name}</h4>
            <p>${store.store_salary}</p>
            <p>${store.work_days}</p>
        </div>
    `;
    $('#store-list').append(storeItem);
}

function changeBackgroundColor(element, onHover) {
    if (onHover) {
        element.style.backgroundColor = '#f0f0f0'; // 호버 시 배경색 변경
    } else {
        element.style.backgroundColor = ''; // 마우스가 떠나면 원래대로
    }
}

function goToDetail(storeId) {
    window.location.href = `/detailStore/${storeId}`;
}