content = {
    
    map: null,
    tooltip: null,
    options: {
        play: true,
        latitude: -23.5451264,
        longitude: -46.4743351,
        elementId: "map",
        zoom: 14
    },
    items: {},
    
    setup: function() {
        
        var position = new google.maps.LatLng(content.options.latitude, content.options.longitude);
        
        content.map = new google.maps.Map(
            document.getElementById(content.options.elementId), 
            {
                zoom: content.options.zoom,
                center: position, 
                mapTypeId: google.maps.MapTypeId.ROADMAP
            });
            
        content.tooltip = new google.maps.InfoWindow({ maxWidth: 250 });
        
    },
    
    addMarker: function(latitude, longitude) {
        
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(latitude, longitude),
            map: content.map
        });
        
        return marker;
    },
    
    addItem: function(item) {
        if (content.items[item.id] == null) {
            marker = content.addMarker(item.data.latitude, item.data.longitude);
            marker.setTitle(item.id);
            item.marker = marker;
            content.items[item.id] = item;
            google.maps.event.addListener(marker, "click", function() {
                content.showItem(marker.getTitle());
            });
        }
    },
    
    showItem: function(id) {
        item = content.items[id];
        if (item != null) {
            content.tooltip.setContent(tmpl("info", item.data));
            content.tooltip.open(content.map, item.marker);
        }
    }
     
}

	
$(document).ready(function() {
    
    content.setup();

    eb = new vertx.EventBus(
      window.location.protocol + '//' +
      window.location.hostname + ':' +
      8000 + '/event');

    eb.onopen = function() {
        console.log('open');
        eb.registerHandler('MyChannel', function(message) {
            var data = JSON.parse(message);

            item = { 
                id: data.id,
                data: {
                    latitude: data.location.latitude, 
                    longitude: data.location.longitude,
                    username: data.user.username, 
                    thumbnailWidth: data.images.thumbnail.width, 
                    thumbnailHeight: data.images.thumbnail.height, 
                    thumbnailUrl: data.images.thumbnail.url,
                    caption: data.caption == null ? null : data.caption.text,
                    url: data.link
                }
            }
            content.addItem(item);
        });
    };

    eb.onclose = function() {
        console.log('close');
    };
	                   
});

