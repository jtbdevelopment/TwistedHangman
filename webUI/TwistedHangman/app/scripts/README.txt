The initialization (and re-initialization) of the services and controllers follows a staggered load process.

1.  playerServices makes http request to get player details
2.  it publishes playerLoaded when complete
3.  On playerLoaded, liveGameFeed establishes socket
4.  when socket is opened, it publishes liveFeedEstablished
5.  when this is established gameCache loads in games
6.  when complete gameCache publishes gameCachesLoaded

This chain starts afresh on a player switch
it starts from 3 if the live feed connection is lost and re-opened
it starts from 5 when refresh games are pushed

By listening on correct event, another service or controller can know when data is ready or reloaded and take action

gameFeatures and gamePhases are loaded independently
