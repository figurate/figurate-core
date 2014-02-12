package org.figurate.log

import org.fusesource.jansi.Ansi

/**
 * Created by fortuna on 12/02/14.
 */
class AnsiLogEntry extends FormattedLogEntry {

    Ansi.Color colour

    @Override
    String getMessage(Object... args) {
        return String.format("${colour ? colour.fg() : ''} $template", args)
    }
}
