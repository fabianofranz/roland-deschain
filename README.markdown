## Roland Deschain mashup sample app

### "The doorway was the Lady's eyes. Roland Deschain was looking through them".


Roland Deschain is a sample app useful for demo purposes. Receives events from Instagram in real-time and shows in a map the location where every picture is being taken. Written in Java with Vert.x and deployed live on [**OpenShift**](http://openshift.com), the PaaS (Platform as a Service) by [**Red Hat**](http://redhat.com).

See it live:

**http://jbossvertx-ffranz.rhcloud.com/**

Unstable, a few things to do:

- Automatically subscribe to Instagram API geographies (based on locations.json)
- Responsiveness
- Upgrade to Google Maps API v3
- Pause, rewind, forward, play
- Multiple geographies with filters
- Better cache architecture, maybe Redis
- Etc

