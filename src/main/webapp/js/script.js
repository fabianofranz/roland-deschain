content = {
    
    map: null,
    tooltip: null,
    options: {
        play: true,
        latitude: 40.778281,
        longitude: -73.969878,
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
                    
    $.stream.setup({enableXDR: true});

    $.stream("../async", {
        
        type: "http",
        dataType: "json",
        context: $("#map")[0],
        
        open: function(event, stream) {
            stream.send({});
        },
        
        message: function(event) {
            
            $.each(event.data, function(i1, update) { 
                
                if (update.object == "geography") {
                    
                    $.getJSON("../details?object=" + update.object_id, function(instagrams) {
                        
                        $.each(instagrams.data, function(i2, instagram) {
                            
                            item = { 
                                id: instagram.id,
                                data: {
                                    latitude: instagram.location.latitude, 
                                    longitude: instagram.location.longitude,
                                    username: instagram.user.username, 
                                    thumbnailWidth: instagram.images.thumbnail.width, 
                                    thumbnailHeight: instagram.images.thumbnail.height, 
                                    thumbnailUrl: instagram.images.thumbnail.url,
                                    caption: instagram.caption == null ? null : instagram.caption.text,
                                    url: instagram.link
                                }
                            }
                            
                            content.addItem(item);
                            if (content.options.play) content.showItem(item.id);
                                
                        });
                    });
                }
            });
            
        },
        
        error: function() {
            alert("error");
        },
        
        close: function() {
        }
        
    });
	                   
});

