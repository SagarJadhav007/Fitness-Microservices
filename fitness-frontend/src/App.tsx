import { BrowserRouter as Router , Navigate , Route,Routes,useLocation} from "react-router"
import { useContext, useEffect, useState } from "react"
import { AuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux";
import { setCredentials } from "./store/authSlice";
import { Button } from "@mui/material";
import type { User } from "./store/authSlice";
function App() {
  const {token, tokenData,logIn,logOut} = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState<boolean>(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({token , user: tokenData as NonNullable<User>
    }));
    setAuthReady(true);
  }
  },[token, tokenData, dispatch]);

  return (
    <Router>
      {!token ?(
      <Button variant ="contained" color="primary" 
              onClick={()=>{
                logIn();
              }}>LOGIN</Button>):(
                <div>
                  <pre>{JSON.stringify(tokenData,null ,2)}</pre>
                </div>
              )}
    </Router>
  )
}

export default App
