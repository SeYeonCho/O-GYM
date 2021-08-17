import React from 'react'
import "./profile.css"
import crossFit from '../../assets/pages/mainPage/carousel/crossfit.jpg';
import ProfileRightTrainer from '../../components/organisms/ProfileRight/ProfileRightTrainer';
import ProfileDetailTrainer from '../../components/organisms/ProfileDetail/ProfileDetailTrainer';
import { useEffect, useState } from 'react';
import axios from 'axios'
import ProfileDetailStudent from '../../components/organisms/ProfileDetail/ProfileDetailStudent';
import ProfileRightStudent from '../../components/organisms/ProfileRight/ProfileRightStudent';

function Profile() {
  const [userInfo, setUserInfo] = useState({
    username: '',
    nickname: '',
    email: '',
    tel: '',
    major: '',
    description: '',
    certificates: [],
    careers: [],
    gender: "",
    age: 0,
    snss: [],
    role: {},
    weights: {},
    heights: {}
  });

  let accessToken = localStorage.getItem('accessToken');
  
  useEffect(() => {
    axios.get("https://i5b305.p.ssafy.io/api/user/username,nickname,email,tel,major,description,certificates,careers,gender,age,snss,role,weights,heights", {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      console.log(response.data.data);
      setUserInfo(response.data.data);
    })

  }, [])
  return (
    <>
      <div className="profile">
        <div className="profileRight">
          <div className="profileRightTop">
            <div className="profileCover">
            <img className="profileCoverImg" src={crossFit} alt="" />
            <img className="profileUserImg" src="https://imgtag.co.kr/images/210729/210729_125520/3EYZwp.jpg" alt="" />
            </div>
            <div className="profileInfo">
              <h4 className="profileInfoName">{userInfo.nickname}</h4>
              <span className="profileInfoDesc">{userInfo.description}</span>
            </div>
          </div>
          <div className="profileRightBottom">
            <div style={{ flex: 5.5 }}>
              { userInfo.role["authorityName"] === "ROLE_PTTEACHER" ?
              <ProfileDetailTrainer userInfo={userInfo} /> :
              <ProfileDetailStudent userInfo={userInfo}/>
            }              
            </div>
            <div style={{ flex: 3.5 }}>
              { userInfo.role["authorityName"] === "ROLE_PTTEACHER" ?
                <ProfileRightTrainer /> :
                <ProfileRightStudent />
              }       
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default Profile
