import styles from './CelebratePron.module.css'
import StarBorderIcon from '@mui/icons-material/StarBorder';
import ReplayIcon from '@mui/icons-material/Replay';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { blue } from '@mui/material/colors';
import { useDispatch } from "react-redux";
import { changeCorrect } from "../../store/pron";



function CelebratePron() {
  const dispatch = useDispatch()

  return (
    <div className={styles.celebrateBox}>
      <p className={styles.celebrateText}>참 잘했어요!</p>
      <div className={styles.wordBox}>
        <p className={styles.word}>돌멩이</p>
        <p className={styles.pron}>[돌ː멩이]</p>
        <div className={styles.icons}>
          <StarBorderIcon sx={{ fontSize: 50, color: blue[600] }} />
          <ReplayIcon onClick={()=>{dispatch(changeCorrect(false))}} 
          sx={{ fontSize: 50, color: blue[600] }} />
          <ArrowForwardIosIcon onClick={()=>{dispatch(changeCorrect(false))}}
          sx={{ fontSize: 50, color: blue[600] }} />
        </div>
      </div>
    </div>
  )
}

export default CelebratePron