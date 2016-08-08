## Roland Deschain sample app

### The doorway was the Lady's eyes. Roland Deschain was looking through them.

Roland Deschain is a sample app useful for demo purposes. Receives events from Instagram in real-time and shows in a map the location where every picture is being taken. Written in Java with Vert.x, can be deployed on [**OpenShift 3**](http://openshift.com) by [**Red Hat**](http://redhat.com).

#### Running on OpenShift 3

Start by creating a new project and loading all the required resources from the [OpenShift S2I Builder for Vert.x](https://github.com/vert-x3/vertx-openshift-s2i), e.g.:

```
oc create -f https://raw.githubusercontent.com/vert-x3/vertx-openshift-s2i/master/vertx-s2i-all.json
```

Build a new Vert.x image:

```
oc start-build vertx-s2i --follow
```

And finally use `oc new-app` to create the Vert.x application based in the `vertx-helloworld-maven` template that is already stored in your project. Provide values to the expected environment variables such as `GIT_URI`, `GIT_REF`, and `CONTEXT_DIR` to point to the repository of the application.

```
oc new-app -pGIT_URI=https://github.com/fabianofranz/roland-deschain -pGIT_REF=master -pCONTEXT_DIR=/ --template=vertx-helloworld-maven
```

#### TODO

- Automatically subscribe to Instagram API geographies (based on locations.json)
- Responsiveness
- Upgrade to Google Maps API v3
- Pause, rewind, forward, play
- Multiple geographies with filters
- Better caching architecture, maybe Redis
- Etc
