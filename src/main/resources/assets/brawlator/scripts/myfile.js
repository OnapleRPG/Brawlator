

var onDeath = listen( function(entity,player,event) {
    print(" die event catched", entity);
});
var onDamaged = listen( function(entity, player, event) {
    print("damaged event catched", entity);
});
