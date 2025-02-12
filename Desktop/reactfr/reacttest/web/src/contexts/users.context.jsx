import { createContext, useState, useEffect } from 'react';

import { createUserDocumentFromAuth, onAuthStateChangedListener } from '../utils/firebase/firebase.utils';


//The value we want to access
export const UserContext = createContext({
        currentUser: null,
        setCurrentUser: () => null,
    })

export const UserProvider = ({children}) => {
    const [currentUser, setCurrentUser] = useState('steve');
    const value = { currentUser, setCurrentUser};

    useEffect(() => {
       const unsubscribe = onAuthStateChangedListener((user) => {
        if(user)
        {
            createUserDocumentFromAuth(user);
        }
        setCurrentUser(user);
    })
       return unsubscribe;
    },[]);

    return (
        <UserContext.Provider value={value}>{children}</UserContext.Provider>
    )
    }


