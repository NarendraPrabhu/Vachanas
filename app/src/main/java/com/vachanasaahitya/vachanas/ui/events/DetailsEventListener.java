package com.vachanasaahitya.vachanas.ui.events;

import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;

public interface DetailsEventListener {
    void copy(DetailsHolder details);
    void share(DetailsHolder details);
    void check(DetailsHolder details, boolean b);
}
