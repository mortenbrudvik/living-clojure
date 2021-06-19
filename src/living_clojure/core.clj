(ns living-clojure.core)

42
(/ 2 3)
(/ 2 3.0)
"jam"
:jam
\j 
true
nil
(+ 1 1)
(+ 1 (+ 8 3))
1

; #############################################
; Collections (Immutable)
; #############################################
; Lists
'(1 2 'fruit' :sun)
'(1, 2, 'fruit', :sun) ; Commas is ignored
(first '(1 2 'fruit' :sun))
(rest '(1 2 'fruit' :sun))
(first (rest '(1 2 'fruit' :sun)))
(cons 5 '()) ; Add to a list
(cons 5 '(1 2 'fruit' :sun)) ; Add to a list - at beginning
(conj '(1 2 'fruit' :sun) 5 3) ; Add multiple - at beginning

; Vectors - Works similar to list but with index access
[1 2 :bag 'sun']
(nth [1 2 :bag 'sun'] 2) ; Get item at an index
(last [1 2 :bag 'sun']) ; Better performance than list
(count [1 2 :bag 'sun'])
(conj '(1 2 'fruit' :sun) 5 3) ; Add multiple - at end of list

; Maps - key-value pairs
{:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'}
(get {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car2) ;  Get an item from map
(get {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car4 'Ford') ;  Default value
(:car2 {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'}) ;  Use the key only to get value
(keys {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'})
(vals {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'})
(assoc {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car2 'Audi') ; update a value
(dissoc {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car2) ; Remove key-value pair
(merge {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'}
       {:car4 'Audi', :car5 'Ford'}
       {:car6 'Saab'})

; Sets - Unique collection of data



 