package com.ikkerens.ils.commands;

import com.ikkerens.ils.ILSPlugin;
import com.ikkerens.ils.model.Lock;
import com.mbserver.api.events.BlockInteractEvent;
import com.mbserver.api.game.Player;

public class AddMember extends Command implements InteractionHandler {
    private static final String ADDMEM_KEY = "ILockStuff.MemberManage";

    @Override
    public String getHelp() {
        return "Adds a member to your lock.";
    }

    @Override
    public void executeCommand( final Player player, final String[] args ) {
        if ( args.length == 0 )
            player.sendMessage( "Please enter a name: /ils addmember <playername>" );
        else {
            player.setMetaData( ILSPlugin.AWAITING_INTERACT_KEY, this );
            player.setMetaData( ADDMEM_KEY, args[ 0 ] );
            player.sendMessage( "Please interact with the block you wish to add a member too." );
        }
    }

    @Override
    public void onInteract( final BlockInteractEvent event ) {
        final Lock lock = this.plugin.getDatabase().getLock( event.getLocation() );

        if ( lock == null ) {
            event.getPlayer().sendMessage( "That block is not locked!" );
            return;
        }

        if ( !lock.isOwner( event.getPlayer().getLoginName() ) && !event.getPlayer().hasPermission( "ikkerens.ilockstuff.admin" ) ) {
            event.getPlayer().sendMessage( "You do not own this lock!" );
            return;
        }

        final String memName = event.getPlayer().getMetaData( ADDMEM_KEY, null );

        if ( memName != null ) {
            lock.addMember( memName );
            event.getPlayer().sendMessage( String.format( "Player %s has been added as a member to this lock.", memName ) );
        } else
            event.getPlayer().sendMessage( "Oops! Something went wrong!" );
    }

}
