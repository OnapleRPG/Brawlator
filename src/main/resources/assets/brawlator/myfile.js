

var onDeath = listen( function(entity,player,event) {
    print(" die event catched", event);
});
var onDamaged = listen( function(entity, player, event) {
    print("damaged event catched", event);
});
