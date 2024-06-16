let markers = [];

// 지도 초기화 함수
function initMap() {
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
    addMapEventListeners(map);
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
    addMapEventListeners(map);
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
            response.forEach((store, index) => {
                var marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(store.area_lat, store.area_lng),
                    map: map
                });
                markers.push({marker: marker, storeId: store.store_id});
                appendStoreToList(store, index);
            });
        },
        error: function(xhr, status, error) {
            console.error('백엔드로 좌표 전달 실패:', error);
        }
    });
}

function addMapEventListeners(map) {
    naver.maps.Event.addListener(map, "dragend", () => {
        getStore(map);
    });
    naver.maps.Event.addListener(map, "zoom_changed", () => {
        getStore(map);
    });
}

function clearMarkers() {
    markers.forEach(markerObj => markerObj.marker.setMap(null));
    markers = [];
}

function clearStoreList() {
    $('#store-list').empty();
}

function appendStoreToList(store, index) {
    const storeItem = `
        <div class="row store-item" style="cursor: pointer;"
            onmouseover="highlightMarker(${index}, true)"
            onmouseout="highlightMarker(${index}, false)"
            onclick="goToDetail(${store.store_id})">
            <div class="col-6 store-image-container">
                <img src="/images/${store.image_url}" alt="${store.store_name}" style="width: 100%; height: auto;" class="store-image">
            </div>
            <div class="col store-details">
                <h4 class="store-name">${store.store_name}</h4>
                <p class="store-salary">시급: ${store.store_salary}</p>
                <p class="store-work-days">근무요일: ${store.work_days}</p>
            </div>
        </div>
    `;
    $('#store-list').append(storeItem);
}

function highlightMarker(index, onHover) {
    const markerObj = markers[index];
    if (onHover) {
        markerObj.marker.setAnimation(naver.maps.Animation.BOUNCE);
    } else {
        markerObj.marker.setAnimation(null);
    }
}

function goToDetail(storeId) {
    window.location.href = `/detailStore/${storeId}`;
}