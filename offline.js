(function () {
    "use strict";
    /* global importScripts */
    /* global self */
    /* global caches */
    /* global fetch */
    /* global URL */


    var projectDir = "/scalajs-offline"
    
    // Include SW cache polyfill  this script will be downloaded and cached by default
    importScripts(projectDir+"/js/serviceworker-cache-polyfill.js");

    //version
    
    var version = "0.0.9"

    // Cache name definitions
    var cacheNameStatic = "scalajs-offline-"+version;
    var cacheNameGithub = "scalajs-offline-github";
        
    var currentCacheNames = [
        cacheNameStatic,
        cacheNameGithub
    ];


    // A new ServiceWorker has been registered
    self.addEventListener("install", function (event) {
        console.log("Installing SW ..  " + new Date())
        event.waitUntil(
            caches.open(cacheNameStatic)
                .then(function (cache) {
                    return cache.addAll([
                        projectDir+"/index.html",
                        projectDir+"/js/scalajs-offline-opt.js",
                        projectDir+"/js/scalajs-offline-jsdeps.js"
                    ]);
                })
        );
    });


    // New service worker is activated at this point ( now you can safely perform deleting/migrating old cache)
    self.addEventListener("activate", function (event) {
        event.waitUntil(
            caches.keys()
                .then(function (cacheNames) {
                    return Promise.all(
                        cacheNames.map(function (cacheName) {
                            if (currentCacheNames.indexOf(cacheName) === -1) {
                                return caches.delete(cacheName);
                            }
                        })
                    );
                })
        );
    });

    // when a network request made from app
    self.addEventListener("fetch", function (event) {
        var requestURL = new URL(event.request.url);
        if (requestURL.hostname.indexOf(".github.com") > -1) {
           event.respondWith(githubAPIResponse(event.request))
        } else {
            event.respondWith(
                caches.match(event.request)
                    .then(function (response) {
                        if (response) {  // if we cached already just send that response back
                            return response;
                        }
                        var fetchRequest = event.request.clone();
                        return fetch(fetchRequest).then( // if not cached get from network return it and then cache it
                            function (response) {
                                var shouldCache = false;
                                if (response.type === "basic" && response.status === 200) {
                                    shouldCache = cacheNameStatic;
                                }
                                if (shouldCache) {
                                    var responseToCache = response.clone();
                                    caches.open(shouldCache)
                                        .then(function (cache) {
                                            var cacheRequest = event.request.clone();
                                            cache.put(cacheRequest, responseToCache);
                                        });
                                }
                                return response;
                            }
                        );
                    })
            );
        }
    });

    /**
     *  get scalajs github repos.
      * @param request
     * @returns {*}
     */
  function githubAPIResponse(request) {
      if (request.headers.get('Accept') == 'x-cache/only') {
          return caches.match(request).then(function(response) {
              return response;
          });
      } else {
          return fetch(request.clone()).then(function(response) {
              if(response.status === 200 ) { // only cache successful responses , as github supports CORS we can check status of response for non supported cors origins we'll get opaque response
                  return caches.open(cacheNameGithub).then(function(cache) {
                      cache.put(request,response.clone())
                      return response;
                  }); 
              } else {
                  return response;
              }
              
          });
      }
  }  

})();