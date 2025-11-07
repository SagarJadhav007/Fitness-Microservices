import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

export type User = { sub?: string; [key: string]: any } | null;

interface AuthState {
  user: User;
  token: string | null;
  userId: string | null;
}

const storedUser = localStorage.getItem("user");
const initialUser: User = storedUser ? JSON.parse(storedUser) : null;
const initialToken = localStorage.getItem("token");
const initialUserId = localStorage.getItem("userId");

const initialState: AuthState = {
  user: initialUser,
  token: initialToken,
  userId: initialUserId,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{ user: NonNullable<User>; token: string }>
    ) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.userId = action.payload.user.sub ?? null;

      localStorage.setItem("token", action.payload.token);
      localStorage.setItem("user", JSON.stringify(action.payload.user));
      if (action.payload.user.sub) {
        localStorage.setItem("userId", action.payload.user.sub);
      } else {
        localStorage.removeItem("userId");
      }
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.userId = null;

      localStorage.removeItem("token");
      localStorage.removeItem("user");
      localStorage.removeItem("userId");
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;
