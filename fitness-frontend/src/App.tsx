// App.tsx
import {
  BrowserRouter as Router,
  Navigate,
  Route,
  Routes,
} from "react-router"; 
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { setCredentials } from "./store/authSlice";
import { Box, Button } from "@mui/material";
import type { User } from "./store/authSlice";

import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivitiesDetail from "./components/ActivityDetail";

// ✅ FIXED: TSX component MUST return JSX
const ActivitiesPage = () => {
  return (
    <Box component="section" sx={{ }}>
      <ActivityForm onActivitiesAdded={() => window.location.reload()} />
      <ActivityList />
    </Box>
  );
};

function App() {
  const { token, tokenData, logIn, logOut } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState<boolean>(false);

  useEffect(() => {
    if (token && tokenData) {
      dispatch(
        setCredentials({
          token,
          user: tokenData as User, // ✅ TS-safe casting
        })
      );
    }
    setAuthReady(true);
  }, [token, tokenData, dispatch]);

  return (
    <Router>
      {!token ? (
        <Button
          variant="contained"
          color="primary"
          onClick={() => logIn()}
        >
          LOGIN
        </Button>
      ) : (
        <Box component="section" sx={{ p: 2, border: "1px dashed grey" }}>
          <Routes>
            <Route path="/activities" element={<ActivitiesPage />} />
            <Route path="/activities/:id" element={<ActivitiesDetail />} />

            <Route
              path="/"
              element={
                token ? (
                  <Navigate to="/activities" replace />
                ) : (
                  <div>Welcome please login</div>
                )
              }
            />
          </Routes>
        </Box>
      )}
    </Router>
  );
}

export default App;
